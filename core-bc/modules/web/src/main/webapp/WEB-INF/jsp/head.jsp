<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Vertikala prioriteringar</title>
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.2.0/build/cssreset/reset-context-min.css">

<style type="text/css">

.main {
  width: 100%;
}

.sector-node div {
  padding-left: 10px;
}

.sectors {
  padding-right: 10px;
  border: thin solid gray;
  margin-right: 10px;
}

.button-row {
  width: 100%;
}

.sectors {
  //background-color: orange;
  min-width: 100px;
  float: left;
  height: 100%;
}

.button-row form {
  display: inline;
}

.button-row .help {
  float: right;
}

.conf-columns {
  left: 2em;
}

.cost {
  left: 2em;
}

.sectorsAndButtons {
  #white-space: nowrap;
}

.button-row .export-data-buttons {
  padding-left: 2em;
  padding-right: 2em;
}

.rowsAndButtons {
  XXXbackground-color: #eeeeee;
  float: left;
  max-width: 1000px;
  min-width: 200px;
}

.rowsAndButtons table thead td {
  font-weight: bold;
  padding-right: 1em;
}

.rowsAndButtons table td {
  font-weight: bold;
  padding-right: 1em;
  border-left-color: black;
  border-left-style: solid;
  border-left-width: thin;
  vertical-align: top;
  max-width: 20em;
  overflow: hidden;
}

.diagnosTexts {
  overflow: auto;
  width: 20em;
  white-space: nowrap;
}

.rowsAndButtons table .even {
  background-color: silver;
}

.rowsAndButtons table .odd {
  background-color: #eeeeee;
}

.rowsAndButtons table {
  border-collapse:collapse;
  border:thin black solid;
}

.footer {
  background-color: yellow;
  clear: both
}

#select-prio {
  display: none;
}

.popup-overlay {
  left: 0px; 
  top: 0px; 
  text-align: center; 
  position: absolute; 
  width: 100%; 
  height: 100%;
  opacity:0.4;
  filter:alpha(opacity=40);
  background-color: black;
}

.window {
  position: relative;
  z-index: 100;
}

.popup-overlay .window {
  position: relative; 
  top:10%; 
  background-color: lime; 
  background-repeat: repeat;
  opacity:1.0;
  filter:alpha(opacity=1);
}

.popup-overlay .window form { 
  opacity:1.0;
  filter:alpha(opacity=1);
  background-color: white;
  display: inline;
  width: 100%;
  height: 100%;
}

.select-diagnoses table td {
  vertical-align: top;
  text-align: left;
}

.kod, .kod-label {
  display: block;
}

.prio-view {
  max-width: 600px;
  min-width: 300px;
  text-align: center;
}

.prio-view form {
  text-align: center;
}

.prio-view .values {
  width: 60em;
  XXXheight: 45em;
  background-color: white;
  border-bottom-color: silver;
  border-width: thin;
  border-style: solid;
  border-spacing: 1em;
  margin: auto;
  text-align: left;
  padding: 1em;
  
}


.find-select-code {
  display: block;
  clear:both;
}

.find-select-code .findings {
  overflow: auto;
  max-height: 3em;
}

.find-select-code .selected, .find-select-code .findings {
  float: left;
  width: 50%;
  min-height: 1em;
}

.find-select-code .label, .find-select-code .findings {
  float: right;
  clear: right;
}

.find-select-code .label {
  width: 50%;
  
}

</style>
</head>