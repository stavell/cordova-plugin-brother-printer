var BrotherLabelPrinter = function () {}
BrotherLabelPrinter.prototype = {

    init: function (data, callback) {
        cordova.exec(callbackFn, null, 'BrotherPrinter', 'init', [data]);
    },

    printBase64Image: function (data, callback) {
        if (!data || !data.length) {
            console.log('No data passed in. Expects a bitmap.');
            return
        }
        cordova.exec(callback, function(err){console.log('error: '+err)}, 'BrotherLabelPrinter', 'printBase64Image', [data]);
    },

    getPrinterStatus: function(data, callback) {
        cordova.exec(callback, function(err){console.log('error: '+err)}, 'BrotherLabelPrinter', 'getPrinterStatus', [data]);
    }
}
var plugin = new BrotherLabelPrinter();
module.exports = plugin;
