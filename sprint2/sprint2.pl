%====================================================================================
% sprint2 description   
%====================================================================================
request( loadrequest, loadrequest(OCCUPIED) ).
reply( loadaccepted, loadaccepted(SLOT,HOLD) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON,HOLD) ).  %%for loadrequest
dispatch( pushButton, pushButton(NONE) ).
dispatch( setOccupied, setOccupied(FLAG) ).
dispatch( updateDisplay, updateDisplay(STATE,HOLD,MSG) ).
request( transportContainer, transportContainer(SLOT) ). %cargoservice incarica cargorobot di prelevare il container dall'IOPort, passare da slot5 per la marcatura e depositarlo nello SLOT riservato
reply( transportDone, transportDone(SLOT) ).  %%for transportContainer
reply( transportFailed, transportFailed(CAUSE) ).  %%for transportContainer
request( waitMarking, waitMarking(NONE) ). %cargorobot, depositato il container in slot5, resta bloccato in attesa che cargoservice segnali la fine della marcatura
reply( markingDone, markingDone(NONE) ).  %%for waitMarking
dispatch( robotHalted, robotHalted(CAUSE) ). %cargorobot notifica a cargoservice di essersi fermato per un guasto di RobotSmart26; cargoservice deciderà come procedere
request( moverobot, moverobot(TARGETX,TARGETY,STEPTIME) ). %move from current pos to (TARGETX,TARGETY)
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
%====================================================================================
context(ctxcargoservice, "localhost",  "TCP", "8050").
context(ctxrobotsmart, "127.0.0.1",  "TCP", "8090").
 qactor( robotsmart, ctxrobotsmart, "external").
  qactor( cargoservice, ctxcargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargorobot, ctxcargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( ioport, ctxcargoservice, "it.unibo.ioport.Ioport").
 static(ioport).
