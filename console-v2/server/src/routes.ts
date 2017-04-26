/*******************************************************************************
* Copyright (c) 2011, 2016 Eurotech and/or its affiliates
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Eurotech - initial API and implementation
*
*******************************************************************************/
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
let publicKeyPath = path.resolve(kapuaServerConf.restApi.publicKeyPath);
console.info(`Reading Public Key from: ${publicKeyPath}`);
let publicKey = fs.readFileSync(path.resolve(kapuaServerConf.restApi.publicKeyPath));

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
        public api(req: express.Request & bodyParser.Parsed, res: express.Response) {
            let token = _.replace(<string>req.headers["authorization"], "Bearer ", "");
            if (token) {
                try {
                    let decoded = jwt.verify(token, publicKey);
                } catch (err) {
                    res.sendStatus(401);
                    return;
                }
            }
            let apiUrl = kapuaServerConf.restApi.baseUrl + _.replace(req.url, "/api", "");
            request({
                url: apiUrl,
                method: req.method,
                headers: req.headers,
                body: req.body,
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
            res.sendFile("index.html", { root: path.resolve(__dirname, "../../ui/dist") });
        }
    }
}

export = Route;
