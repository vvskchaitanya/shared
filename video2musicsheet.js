const fs = require('fs');
const youtubedl = require('youtube-dl');
const ffmpeg = require('ffmpeg');
var Jimp = require('jimp');

const keys = ["A-4", "B-4",
    "C-3", "D-3", "E-3", "F-3", "G-3", "A-3", "B-3",
    "C-2", "D-2", "E-2", "F-2", "G-2", "A-2", "B-2",
    "C-1", "D-1", "E-1", "F-1", "G-1", "A-1", "B-1",
    "C0", "D0", "E0", "F0", "G0", "A0", "B0",
    "C1", "D1", "E1", "F1", "G1", "A1", "B1",
    "C2", "D2", "E2", "F2", "G2", "A2", "B2",
    "C3", "D3", "E3", "F3", "G3", "A3", "B3", "C4"];

var output = "";

var threshold = 30;

const readline = require("readline");
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
console.log("Video 2 Music Sheet")
console.log("Convert your favourite keyboard music video to understandable music sheet...")
console.log("1. Download from youtube and process");
console.log("2. Local video file and process");
rl.question("Enter the option: ", function(option) {
    if (option == '1') processVideoFromYoutube();
    else if (option = '2') processLocalVideo();
    else console.log("Good bye...");
});

var processVideo = function(video) {
    var process = new ffmpeg(video);
    process.then(function(video) {
        // Callback mode
        video.fnExtractFrameToJPG('tmp/images', {
            size: '640x180',
            every_n_seconds: 1,
            file_name: 'frame_%03d'
        }, function(error, files) {
            if (!error)
                processImages(files);
            else {
                console.error(error);
                rl.close();
            }
        });
    }, function(err) {
        console.log('Video Processing Error: ' + err);
        rl.close();
    });
}

var processLocalVideo = function() {
    rl.question("Enter the local path of the video?", function(path) {
        processVideo(path);
    });
}
var processVideoFromYoutube = function() {
    rl.question("Enter the youtube URL of the video?", function(url) {
        const video = youtubedl(url);
        video.on('info', function(info) {
            console.log('Video Download Started...')
            console.log('filename: ' + info._filename)
            console.log('size: ' + info.size)
        });
        video.on('end', function() {
            console.log('Video Download Completed');
            console.log('Video Processing Started..');
            processVideo("video.mp4");
        });
        video.pipe(fs.createWriteStream('video.mp4'));

    });
}

rl.on("close", function() {
    fs.writeFile('keys-output.txt', output, function(err) {
        if (err) {
            console.error("Error saving the output file");
        }
        console.log('Saved output to keys-output.txt');
        process.exit(0);
    });
    console.log("\nBYE BYE !!!");

});


var processImages = function(images) {
    console.log(images.length);
    for (var i = 0; i < images.length; i++) {
        Jimp.read(images[i])
            .then(image => {
                // Traversing image y-axis upto 180 
                for (var y = 20; y < 180; y += 20) {
                    var line = "";
                    for (var x = 7; x < 360; x += 7) {
                        var red = Jimp.intToRGBA(image.getPixelColor(x - 3, y)).r;
                        if (red > threshold) {
                            line += keys[x / 7];
                        }
                    }
                    console.log("" + images[i] + "_" + y / 20 + " " + line);
                    output += line + "\n";
                }
            })
            .catch(err => {
                console.error("Error processing image: " + images[i], err);
            });
    }
}
