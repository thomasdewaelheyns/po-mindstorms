// helper functions to update HTML parts
function updateHTML(id, value) { 
  document.getElementById(id).innerHTML = value;
}

function updateStyle(id, style, value ) {
  document.getElementById(id).style[style] = value;
}

function updateClass(id, className) {
  document.getElementById(id).className = className;
}

// public method used by JSONP to update the dashboard
function update( lightValue,      lightColor, 
                 barcode,
                 sonarAngle,      sonarDistance,
                 pushLeft,        pushRight,
                 navigatorEvent,  navigatorSource,
                 navigatorAction, navigatorActionQueue,
                 actionKind,      actionArgument )
{
  // model
  updateLight   ( lightValue, lightColor    );
  updateBarcode ( barcode                   );
  updateSonar   ( sonarAngle, sonarDistance );
  updatePush    ( pushLeft,   pushRight     );
  // navigator
  updateEvent   ( navigatorEvent,   navigatorSource     );
  updatePlan    ( navigatorAction, navigatorActionQueue );
  updateAction  ( actionKind, actionArgument            );
}

var lightHistogram = [];
var sonarHistogram = [];

function updateLight( lightValue, lightColor ) {
  var v = lightColor / 4;
  updateHTML ( "lightValue", lightValue );
  updateStyle( "lightValue", "backgroundColor", "rgb("+v+","+v+","+v+")" );
  updateHTML ( "lightColor", lightColor );
  updateClass( "lightColor", lightColor );
  addColor   (lightColor);
  renderLightHistogram();
}

function updateBarcode(barcode) {
  updateHTML( "barcode", "barcode: " + barcode );
}

function updateSonar(angle, distance) {
  updateHTML( "sonarValue", distance +"cm @ " + angle + "&deg;" );
  addDistance(angle, distance);
  renderSonarHistogram();
}

function updatePush(left, right) {
  updateHTML ( "push-left", left  ? "PUSHED" : "OK" );
  updateClass( "push-left", left  ? "push active" : "push inactive" );

  updateHTML ( "push-right", right ? "PUSHED" : "OK" );
  updateClass( "push-right", right ? "push active" : "push inactive" );
}

function updateEvent( kind, source ) {
  kind   = typeof kind != "undefined" ? kind : "";
  source = typeof source != "undefined" ? source : "";
  updateHTML( "eventKind",   kind );
  updateHTML( "eventSource", "on " + source );
}

function updatePlan( action, actionQueue ) {
  updateHTML( "eventAction", action );
  if( typeof actionQueue == "undefined" ) { return; }
  var actions = actionQueue.split("|");
  var queueHTML = "";
  for( var action in actions ) {
    queueHTML += "<li>" + actions[action] + "</li>";
  }
  updateHTML( "eventActionQueue", queueHTML );
}

function updateAction( kind, argument ) {
  updateHTML( "actionKind", kind );
  updateHTML( "actionArgument", argument );
}

function addDistance(angle, distance) {
  sonarHistogram.unshift([angle,distance]);
  // clean up acient history, keep max values
  var max = 100;
  if( sonarHistogram.length > max ) {
    sonarHistorgram.splice(max, sonarHistogram.length - max);
  }
}

function addColor(color) {
  lightHistogram.unshift(color);
  // clean up acient history, keep max values
  var max = 15;
  if( lightHistogram.length > max ) {
    lightHistorgram.splice(max, lightHistogram.length - max);
  }
}

function renderLightHistogram() {
  var canvas = document.getElementById("lightHistogram").getContext("2d");
  for( var i=0; i<lightHistogram.length; i++ ) {
    canvas.fillStyle = ( lightHistogram[i] == "brown" ? "rgb(209,127,46)" : 
                       lightHistogram[i] );
    canvas.fillRect(i*5,0,5,70);
  }
}

function renderSonarHistogram(hist) {
  var canvas = document.getElementById("sonarHistogram").getContext("2d");
  var opacity = 1;
  canvas.fillStyle = "black";
  canvas.fillRect(0,0,150,150);
  for( var i=0; i<sonarHistogram.length; i++ ) {
    var angle = sonarHistogram[i][0];
    var dist  = sonarHistogram[i][1];
    canvas.strokeStyle = "rgba(0,255,0," + opacity + ")";
    canvas.beginPath();
    canvas.moveTo(75,125);
    canvas.lineTo(  75 - Math.sin((angle)/180*Math.PI) * dist, 
    125 - Math.cos((angle)/180*Math.PI) * dist );
    canvas.stroke();
    opacity -= 0.1   ;
  }
}
