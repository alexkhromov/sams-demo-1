import React from 'react';
import JWT from '../lib/JWT';

const userInitialState = {
    loggedIn: false,
    username: "",
    roles: []
};

function userReducer(state, action) {
    switch (action.type) {
        case 'signin':
            JWT.setStorage(action.token);
            return {
                loggedIn: true,
                username: JWT.payload.metadata.username,
                roles: JWT.payload.roles
            };
        case 'signout':
            JWT.clearStorage();
            return userInitialState;
        default:
            throw new Error();
    }
}

export function useUserReducer() {
    let initialState = userInitialState;
    if (!JWT.tokenExpired()) {
        initialState = {
            loggedIn: true,
            username: JWT.payload.metadata.username,
            roles: JWT.payload.roles
        };
    }
    return React.useReducer(userReducer, initialState);
}