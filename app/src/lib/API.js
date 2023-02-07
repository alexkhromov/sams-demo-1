import { QUESTIONS_ENDPOINT } from './Constraints';
import JWT from './JWT';

function buildParams(params) {
    let paramsArray = [];

    const entries = Object.entries(params);

    for (const [key, value] of entries) {
        if (value !== null && value !== undefined) {
            paramsArray.push(`${key}=${encodeURI(value)}`);
        }
    }

    return paramsArray ? `?${paramsArray.join('&')}` : '';
}

const METHOD = {
    GET: 'GET',
    POST: 'POST',
    PUT: 'PUT',
    DELETE: 'DELETE'
}

function doRequest(
    method, 
    { 
        endpoint = QUESTIONS_ENDPOINT,
        url = "", 
        params = {}, 
        headers = new Headers(), 
        body = null 
    }
) {
    headers.set('Authorization', JWT.getStorage());
    const init = {
        method: method,
        headers: headers
    };
    if (body) {
        init.body = body;
    }
    return fetch(
        `${endpoint}${url ? `/${url}` : ""}${buildParams(params)}`,
        init
    );

}

export class API {
    
    static get(args) {
        return doRequest(METHOD.GET, args);
    }

    static post(args) {
        return doRequest(METHOD.POST, args);
    }

    static put(args) {
        return doRequest(METHOD.PUT, args);
    }

    static delete(args) {
        return doRequest(METHOD.DELETE, args);
    }

}
