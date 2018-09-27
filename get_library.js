'use strict';

var version = '23.53.589.4-arm';
var beta    = '';

var exec = require('child_process').exec;
var fs   = require('fs');
var wget = require('node-wget');

function prepareLibrary (filePath) {
    try {
        fs.unlinkSync('./libs/' + filePath);
    }
    catch (e) {
        console.log('No previous file');
    }
    fs.renameSync(filePath, './libs/' + filePath);
    console.log('Library baked');
}

function handleDownloaded (error, data) {
    if (error) {
        console.error('Failed downloading file');
        console.error(error);
    }
    else {
        console.log('File downloaded successfully');
        prepareLibrary(data.filepath);
    }
}

function downloadLibrary () {
    var url = 'https://download.01.org/crosswalk/releases/crosswalk/android/maven2/org/xwalk/xwalk_core_library' + beta + '/' + version + '/xwalk_core_library_' + beta + '-' + version + '.aar';
    console.log('Downloading file...');
    wget(url, handleDownloaded);
}

downloadLibrary();
