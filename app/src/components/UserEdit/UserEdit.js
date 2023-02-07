import React from 'react';
import { Redirect } from 'react-router-dom';
import LocaleContext from '../../contexts/LocaleContext';
import { API } from '../../lib/API';
import { ROLE, STATUS, USERS_ENDPOINT } from '../../lib/Constraints';
import { fireGlobalErrors } from '../../lib/Errors';
import UserForm from './UserForm';

function UserEdit({ match }) {
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const [user, setUser] = React.useState(undefined);
    const userId = match.params.userId || 0;
    const locale = React.useContext(LocaleContext);

    const okCallback = React.useCallback((user) => {
        let headers = new Headers();
        headers.set('Content-Type', 'application/json');
        headers.set('Accept-Language', locale.full);

        let roles = [];

        for (let [key, {role, checked}] of Object.entries(user.roles)) {
            if (checked) {
                roles.push(role); 
            }
        }

        let body = {
            email: user.email,
            username: user.username,
            roles: roles,
            isDeleted: user.isDeleted
        };

        API.put({
            endpoint: USERS_ENDPOINT,
            url: `${userId}`,
            headers: headers,
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.ok) {
                setShouldRedirect(true);
            } else {
                response.json().then((result) => {
                    if (result.status === STATUS.FAILURE) {
                        fireGlobalErrors(result.errorData);
                    }
                });
            }
        });

    }, [userId, locale.full]);

    const cancelCallback = React.useCallback(() => {
        setShouldRedirect(true);
    }, []);

    React.useEffect(() => {

        let headers = new Headers();
        headers.set('Accept-Language', locale.full);
        
        API.get({
            endpoint: USERS_ENDPOINT,
            url: `${userId}`,
            headers: headers
        }).then((response) => {
            if (response.ok) {
                response.json().then((value) => {
                    let data = value.data[0];

                    let roles = {};

                    for (const [key, value] of Object.entries(ROLE)) {
                        roles[key] = {role: value, checked: data.roles.includes(value)}; 
                    }            

                    let user = {
                        email: data.email || "",
                        username: data.username || "",
                        isDeleted: data.isDeleted || false,
                        roles: roles
                    };
                    
                    setUser(user);
                });
            } else {
                response.json().then((result) => {
                    if (result.status === STATUS.FAILURE) {
                        fireGlobalErrors(result.errorData);
                    }
                });
                setUser({});
            }
        });

    }, [userId, locale.full]);

    return (
        <>
            <div className="container">
                <UserForm
                    okCallback={okCallback}
                    cancelCallback={cancelCallback}
                    initState={user}
                />
            </div>
            
            {shouldRedirect && <Redirect to="/" />}
        </>
    );

}

export default UserEdit;