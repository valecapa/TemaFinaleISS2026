%====================================================================================
% sprint1 description   
%====================================================================================
request( loadrequest, loadrequest(OCCUPIED) ).
reply( loadaccepted, loadaccepted(SLOT,HOLD) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON,HOLD) ).  %%for loadrequest
dispatch( blinkLed, blinkLed(STATE,HOLD) ).
dispatch( pushButton, pushButton(NONE) ).
dispatch( setOccupied, setOccupied(FLAG) ).
dispatch( updateDisplay, updateDisplay(STATE,HOLD,MSG) ).
%====================================================================================
context(ctxcargoservice, "localhost",  "TCP", "8050").
 qactor( cargoservice, ctxcargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( ioport, ctxcargoservice, "it.unibo.ioport.Ioport").
 static(ioport).
  qactor( testbutton, ctxcargoservice, "it.unibo.testbutton.Testbutton").
 static(testbutton).
  qactor( cargoservicecaller, ctxcargoservice, "it.unibo.cargoservicecaller.Cargoservicecaller").
 static(cargoservicecaller).
