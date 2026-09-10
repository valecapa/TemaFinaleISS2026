%====================================================================================
% sprint2 description   
%====================================================================================
request( loadrequest, loadrequest(OCCUPIED) ).
reply( loadaccepted, loadaccepted(SLOT,HOLD) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON,HOLD) ).  %%for loadrequest
dispatch( pushButton, pushButton(NONE) ).
dispatch( setOccupied, setOccupied(FLAG) ).
dispatch( updateDisplay, updateDisplay(STATE,HOLD,MSG) ).
request( transportContainer, transportContainer(SLOT) ).
reply( transportDone, transportDone(SLOT) ).  %%for transportContainer
reply( transportFailed, transportFailed(CAUSE) ).  %%for transportContainer
request( waitMarking, waitMarking(NONE) ).
reply( markingDone, markingDone(NONE) ).  %%for waitMarking
dispatch( robotHalted, robotHalted(CAUSE) ).
request( moverobot, moverobot(TARGETX,TARGETY,STEPTIME) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
%====================================================================================
context(ctxcargoservice, "localhost",  "TCP", "8050").
context(ctxioport, "localhost",  "TCP", "8060").
context(ctxrobotsmart, "127.0.0.1",  "TCP", "8090").
 qactor( robotsmart, ctxrobotsmart, "external").
  qactor( cargoservice, ctxcargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargorobot, ctxcargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( ioport, ctxioport, "it.unibo.ioport.Ioport").
 static(ioport).
