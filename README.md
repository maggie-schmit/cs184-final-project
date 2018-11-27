# Setup

## Dev environment

### Dependencies

* Node.js version 8+
* Npm

### Running the server

#### First time setup

1. `mkdir ~/.aws`
2. Ask someone who's already got a working copy of the project for the AWS access key (id and secret).
3. Add the access key to `~/.aws/credentials` in the following format:
```[cs184]
aws_access_key_id = <INSERT YOUR KEY ID HERE>
aws_secret_access_key = <ACCESS KEY SECRET HERE>```

1. `cd server`
2. If running for the first time on this machine, `npm install`
3. `npm start`
