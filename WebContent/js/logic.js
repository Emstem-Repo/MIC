// Variables
var lastScannedBarCode = "";
var listOfScannedBarCodes = [];

// Global event handler
document.onkeypress = onGlobalKeyPressed;

function onGlobalKeyPressed(e) {
    var charCode = (typeof e.which == "number") ? e.which : e.keyCode;

    if (charCode != 13) { // ascii 13 is return key
        lastScannedBarCode += String.fromCharCode(charCode);
    } else { // barcode reader indicate code finished with "enter"
        var lastCode = lastScannedBarCode;

        

        lastScannedBarCode = ""; // zero out last code (so we do not keep adding)
    }    
}






