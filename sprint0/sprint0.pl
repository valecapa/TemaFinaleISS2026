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
  qactor( sonarproxy, ctxcargoservice, "it.unibo.sonarproxy.Sonarproxy").
 static(sonarproxy).
  qactor( ioporthandler, ctxcargoservice, "it.unibo.ioporthandler.Ioporthandler").
 static(ioporthandler).
