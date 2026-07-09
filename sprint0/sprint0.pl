%====================================================================================
% sprint0 description   
%====================================================================================
request( loadrequest, loadrequest(none) ).
reply( loadaccepted, loadaccepted(SLOTNAME) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON) ).  %%for loadrequest
dispatch( blinkLed, blinkLed(STATE) ).
dispatch( updateDisplay, updateDisplay(MSG) ).
event( sonarcontainer, sonarcontainer(D) ).
event( sonarfault, sonarfault(D) ).
%====================================================================================
context(ctxcargoservice, "localhost",  "TCP", "8050").
 qactor( cargoservice, ctxcargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargorobot, ctxcargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( sonar, ctxcargoservice, "it.unibo.sonar.Sonar").
 static(sonar).
  qactor( ioport, ctxcargoservice, "it.unibo.ioport.Ioport").
 static(ioport).
