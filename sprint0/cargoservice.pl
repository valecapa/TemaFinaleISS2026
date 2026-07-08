%====================================================================================
% cargoservice description   
%====================================================================================
request( loadrequest, loadrequest(none) ).
reply( loadaccepted, loadaccepted(SLOTNAME) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON) ).  %%for loadrequest
dispatch( moveto, moveto(DEST) ).
dispatch( pick, pick(none) ).
dispatch( place, place(none) ).
dispatch( halt, halt(none) ).
dispatch( resume, resume(none) ).
dispatch( robotdone, robotdone(none) ).
dispatch( markdone, markdone(none) ).
dispatch( updateDisplay, updateDisplay(MSG) ).
dispatch( blinkLed, blinkLed(ONOFF) ).
event( sonarcontainer, sonarcontainer(D) ).
event( sonarfault, sonarfault(D) ).
event( sonarfree, sonarfree(D) ).
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
