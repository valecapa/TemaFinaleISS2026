%====================================================================================
% sprint3 description   
%====================================================================================
mqttBroker("mosquitto", "1883", "cargoservice/sonar/distance").
request( loadrequest, loadrequest(OCCUPIED) ).
reply( loadaccepted, loadaccepted(SLOT,HOLD) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON,HOLD) ).  %%for loadrequest
dispatch( pushButton, pushButton(NONE) ).
dispatch( setOccupied, setOccupied(FLAG) ).
dispatch( updateDisplay, updateDisplay(STATE,HOLD,MSG) ).
request( transportContainer, transportContainer(SLOT,TARGETX,TARGETY) ).
reply( transportDone, transportDone(SLOT) ).  %%for transportContainer
reply( transportFailed, transportFailed(CAUSE) ).  %%for transportContainer
request( waitMarker, waitMarker(NONE) ).
reply( markerDone, markerDone(NONE) ).  %%for waitMarker
dispatch( sonarcontainer, sonarcontainer(DISTANCE) ).
request( moverobot, moverobot(TARGETX,TARGETY,STEPTIME) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
event( sonarreading, sonarreading(DISTANCE) ).
dispatch( sonarfault, sonarfault(CAUSE) ).
dispatch( sonarrestored, sonarrestored(NONE) ).
dispatch( blinkLed, blinkLed(FLAG) ).
event( alarm, alarm(X) ).
dispatch( move, move(M) ).
%====================================================================================
context(ctxcargoservice, "localhost",  "TCP", "8050").
context(ctxrobotsmart, "robotsmart26",  "TCP", "8020").
 qactor( robotsmart, ctxrobotsmart, "external").
  qactor( cargoservice, ctxcargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargorobot, ctxcargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( ioport, ctxcargoservice, "it.unibo.ioport.Ioport").
 static(ioport).
  qactor( sonar, ctxcargoservice, "it.unibo.sonar.Sonar").
 static(sonar).
