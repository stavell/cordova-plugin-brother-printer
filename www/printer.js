var BrotherLabelPrinter = function () {}
BrotherLabelPrinter.prototype = {

    init: function (data, callback) {
        cordova.exec(callback, function(err){
           if(callback) callback(null, err);
           console.log('error: '+err)
       }, 'BrotherLabelPrinter', 'init', [data]);
    },

    printBase64Image: function (data, callback) {
        if (!data || !data.length) {
            if(callback) callback(null, 'No data passed in. Expects a Base64 bitmap.')
            return;
        }
        cordova.exec(callback, function(err){
            if(callback) callback(null, err);
            console.log('error: '+err)
        }, 'BrotherLabelPrinter', 'printBase64Image', [data]);
    },

    getPrinterStatus: function(callback) {
        cordova.exec(callback, function(err) {
            console.log('error: '+err)
            if(callback) callback(null, err);
        }, 'BrotherLabelPrinter', 'getPrinterStatus', []);
    }
}
var plugin = new BrotherLabelPrinter();
module.exports = plugin;
