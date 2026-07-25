(function () {
  const btn = document.getElementById('pushBtn');
  const sensorSwitch = document.getElementById('sensorSwitch');
  const led = document.getElementById('led');
  const stateVal = document.getElementById('stateVal');
  const holdVal = document.getElementById('holdVal');
  const msgVal = document.getElementById('msgVal');
  const connStatus = document.getElementById('connStatus');
  const timerVal = document.getElementById('timerVal');

  let sensorOccupied = false;
  let socket = null;
  const RECONNECT_MS = 2000;

  const TIMER_SECONDS = 30;
  let timerInterval = null;

  function startTimer() {
    let remaining = TIMER_SECONDS;
    timerVal.textContent = remaining + ' s';
    btn.disabled = true;

    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
      remaining -= 1;
      if (remaining <= 0) {
        clearInterval(timerInterval);
        timerVal.textContent = '--';
        if (socket && socket.readyState === WebSocket.OPEN) {
          btn.disabled = false;
        }
        return;
      }
      timerVal.textContent = remaining + ' s';
    }, 1000);
  }

  function setConnStatus(text, lost) {
    connStatus.textContent = text;
    connStatus.style.color = lost ? '#e88' : '';
  }

  function setControlsEnabled(enabled) {
    btn.disabled = !enabled;
    sensorSwitch.disabled = !enabled;
  }

  function connect() {
    socket = new WebSocket('ws://' + window.location.host + '/ws/ioport');

    socket.onopen = () => {
      setConnStatus('connesso', false);
      setControlsEnabled(true);
    };

    socket.onmessage = (event) => {
      let msg;
      try {
        msg = JSON.parse(event.data);
      } catch (e) {
        console.warn('ioport_gui: messaggio non JSON ignorato:', event.data);
        return;
      }
      if (msg.type === 'display') {
        stateVal.textContent = msg.state;
        holdVal.textContent = msg.hold;
        msgVal.textContent = msg.msg;
      } else if (msg.type === 'led') {
        led.classList.toggle('on', !!msg.on);
      } else if (msg.type === 'sensor') {
        sensorOccupied = !!msg.occupied;
        sensorSwitch.classList.toggle('occupied', sensorOccupied);
        sensorSwitch.setAttribute('aria-pressed', String(sensorOccupied));
      }
    };

    socket.onclose = () => {
      setConnStatus('connessione persa, riprovo...', true);
      setControlsEnabled(false);
      setTimeout(connect, RECONNECT_MS);
    };

    socket.onerror = () => {
      socket.close();
    };
  }

  btn.addEventListener('click', () => {
    if (socket && socket.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify({ type: 'pushButton' }));
      startTimer();
    }
  });

  sensorSwitch.addEventListener('click', () => {
    if (!(socket && socket.readyState === WebSocket.OPEN)) return;
    sensorOccupied = !sensorOccupied;
    sensorSwitch.classList.toggle('occupied', sensorOccupied);
    sensorSwitch.setAttribute('aria-pressed', String(sensorOccupied));
    socket.send(JSON.stringify({ type: 'setOccupied', value: sensorOccupied }));
  });

  setControlsEnabled(false);
  connect();
})();