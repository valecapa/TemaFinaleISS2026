%====================================================================================
% sprint1 description   
%====================================================================================
request( loadrequest, loadrequest(NONE) ).
reply( loadaccepted, loadaccepted(SLOT) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON) ).  %%for loadrequest
dispatch( blinkLed, blinkLed(STATE) ).
%====================================================================================
context(ctxcargoservice, "localhost",  "TCP", "8050").
 qactor( cargoservice, ctxcargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargoservicecaller, ctxcargoservice, "it.unibo.cargoservicecaller.Cargoservicecaller").
 static(cargoservicecaller).
