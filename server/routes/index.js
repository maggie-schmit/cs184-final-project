var express = require('express');
var router = express.Router();
const aws = require('aws-sdk');
const uuid = require('uuid/v4');

// AWS setup
aws.config.update({region: 'us-west-2'});
const s3 = new aws.S3();
const rekognition = new aws.Rekognition();
const S3_BUCKET_NAME = 'cs184-faces-2';

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/checkFace', async function (req, res, next) {
  console.log("1");
  // Check input
  if (!req.files.face || !req.files.face.data) {
    res.sendStatus(400);
    return;
  }

  // Add the image to S3
  console.log("2");
  const key = uuid();  // generate a unique key for this image
  const uploadParams = {
    Bucket: S3_BUCKET_NAME,
    Key: key,
    Body: req.files.face.data
  }
  try {
    const s3Response = await s3.putObject(uploadParams).promise();
  } catch (err) {
    res.sendStatus(500);
    console.log(err);
    return;
  }

  // Check if it is in our collection
  console.log("3");
  const params = {
    CollectionId: "members",
    FaceMatchThreshold: 90,
    Image: {
      S3Object: {
        Bucket: S3_BUCKET_NAME,
        Name: key
      }
    },
    MaxFaces: 10
  };
  rekognition.searchFacesByImage(params, function (err, data) {
    console.log("4");
    if (err) {
      res.sendStatus(500);
      console.log(err);
    } else {
      console.log(data);
      res.send(data);
    }
  })
});

module.exports = router;
