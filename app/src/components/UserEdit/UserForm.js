import React from 'react';
import { useTranslation } from 'react-i18next';
import UserContext from '../../contexts/UserContext';
import { ROLE } from '../../lib/Constraints';

function UserForm({ initState, okCallback, cancelCallback }) {
    const initialRoles = React.useMemo(() => {
        let result = {};

        for (const [key, value] of Object.entries(ROLE)) {
            result[key] = {role: value, checked: false}; 
        }

        return result;
    }, []);

    const [user, setUser] = React.useState(initState || {
        email: "", 
        username: "", 
        isDeleted: false, 
        roles: initialRoles
    });

    const { t } = useTranslation('forms');
    const { userState } = React.useContext(UserContext);
    
    const changeHandler = (event) => {
        setUser({...user, [event.target.name]: event.target.value});
    };

    const roleCheckHandler = (event) => {
        setUser({
            ...user, 
            roles: {
                ...user.roles, 
                [event.target.name]: {
                    role: user.roles[event.target.name].role, 
                    checked: !user.roles[event.target.name].checked
                }
            }
        });
    };

    const checkHandler = (event) => {
        setUser({
            ...user,
            [event.target.name]: !user[event.target.name]
        });
    }

    React.useEffect(() => {
        if (initState) {
            setUser(initState);
        }
    }, [initState]);

    const isDisabled = !(
        userState.loggedIn && userState.roles.includes(ROLE.ADMIN)
    );

    return (
        <div className="d-flex flex-column">
            <div className="form-group">
                <label htmlFor="user-email">
                    { t('label.user.email') }
                </label>

                <input 
                    type="text" 
                    className="form-control" 
                    id="user-email" 
                    placeholder={ t('placeholder.user.email') }
                    value={user.email}
                    name="email"
                    onChange={changeHandler}
                    disabled={isDisabled}
                />
            </div>

            <div className="form-group">
                <label htmlFor="user-username">
                    { t('label.user.username') }
                </label>

                <input 
                    type="text" 
                    className="form-control" 
                    id="user-username" 
                    placeholder={ t('placeholder.user.username') }
                    value={user.username}
                    name="username"
                    onChange={changeHandler}
                    disabled={isDisabled}
                />
            </div>

            <div className="input-group my-3">
                <div>
                    {Object.entries(user.roles).map((userRole) => {
                        let [key, {role, checked}] = userRole;
                        return (
                            <div className="form-check" key={key}>
                                <input
                                    type="checkbox"
                                    className="form-check-input"
                                    name={key}
                                    checked={checked}
                                    disabled={isDisabled}
                                    id={key}
                                    onClick={roleCheckHandler}
                                />
                                <label htmlFor={key} className="noselect">
                                    { t(`role.${role}`) }
                                </label>
                            </div>
                        );
                    })}
                    <div className="form-check">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            name="isDeleted"
                            checked={user.isDeleted}
                            disabled={isDisabled}
                            id="isdeleted"
                            onClick={checkHandler}
                        />
                        <label htmlFor="isdeleted" className="noselect">
                            { t(`label.user.isDeleted`) }
                        </label>
                    </div>
                </div>
            </div>
            
            <div className="d-flex flex-row justify-content-end">
                <button 
                    className="btn btn-primary" 
                    onClick={() => {
                        okCallback(user);
                    }}
                    disabled={isDisabled}
                >
                    { t('edit.ok') }
                </button>

                <button 
                    className="btn btn-secondary mx-3" 
                    onClick={cancelCallback}
                >
                    { t('common.cancel') }
                </button>
            </div>
        </div>
    );
}

export default UserForm;