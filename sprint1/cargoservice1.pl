%====================================================================================
% cargoservice1 description   
%====================================================================================
request( loadrequest, loadrequest(none) ).
reply( loadaccepted, loadaccepted(SLOTNAME) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON) ).  %%for loadrequest
dispatch( blinkLed, blinkLed(STATE) ).
%====================================================================================
context(ctxcargoservice, "localhost",  "TCP", "8050").
 qactor( cargoservice, ctxcargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
