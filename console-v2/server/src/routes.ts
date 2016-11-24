"use strict";

import * as express from "express";
import * as path from "path";
import * as request from "request";
import * as bodyParser from "body-parser";
import * as http from "http";
import * as jwt from "jsonwebtoken";
import * as fs from "fs";
import * as _ from "lodash";

let kapuaServerConf = require("../conf/kapua-server.config.json");
let privateKey = fs.readFileSync(path.resolve(__dirname, "../conf/public_key"));

namespace Route {
    export class OAuthLogin {
        public oauthLogin(req: express.Request & bodyParser.Parsed, res: express.Response, next: express.NextFunction) {
            request.post(kapuaServerConf.oauth.tokenEndpoint, {
                form: {
                    "grant_type": "authorization_code",
                    "client_id": kapuaServerConf.oauth.clientId,
                    "code": req.body.code,
                    "redirect_uri": req.body.redirectUri
                },
                json: true
            }, (error: any, tokenResponse: http.IncomingMessage & bodyParser.Parsed, body: any) => {
                request.post(kapuaServerConf.restApi.baseUrl + "/authentication/jwt", {
                    body: {
                        jwt: tokenResponse.body.id_token
                    },
                    json: true
                }, (error: any, restApiResponse: http.IncomingMessage & bodyParser.Parsed, body: any) => {
                    res.json(restApiResponse.body);
                });
            });
        }
    }

    export class Api {
        public api(req: express.Request, res: express.Response) {
            let token = <string>req.headers["x-access-token"];
            if (token) {
                try {
                    let decoded = jwt.verify(token, privateKey);
                } catch (err) {
                    res.sendStatus(401);
                    return;
                }
            }
            let apiUrl = kapuaServerConf.restApi.baseUrl + _.replace(req.path, "/api", "");
            request({
                url: apiUrl,
                method: req.method,
                headers: {
                    "X-Access-Token": token
                },
                json: true
            }, (error: Error, apiResponse: http.IncomingMessage, body: any) => {
                if (error) {
                    res.status(500).send(error.message);
                } else {
                    res.status(apiResponse.statusCode).json(body);
                }
            });
        }
    }

    export class Index {
        public index(req: express.Request, res: express.Response, next: express.NextFunction) {
            res.sendFile("index.html", { root: "ui/dist" });
        }
    }
}

export = Route;
