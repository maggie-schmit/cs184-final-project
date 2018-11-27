var express = require('express');
var router = express.Router();
const aws = require('aws-sdk');

// Rekognition setup
aws.config.update({region: 'us-west-2'});
const rekognition = new aws.Rekognition();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/checkFace', async function (req, res, next) {
  // Check input
  if (!req.files.face) {
    res.sendStatus(400);
    return;
  }
  // Add the image to S3
  console.log(req.files)
  // Check if it is in our collection
  // const params = {
  //   CollectionId: "members",
  //   FaceMatchThreshold: 90,
  //   Image: {
  //     S3Object: {
  //       Bucket: "bucket name here",
  //       Name: "name here"
  //     }
  //   },
  //   MaxFaces: 10
  // };
  // rekognition.searchFacesByImage(params, function (err, data) {
  //   if (err) {
  //     res.sendStatus(500);
  //     console.log(err);
  //   } else {
  //     console.log(data);
  //   }
  // })
});

module.exports = router;
