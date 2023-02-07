import React from 'react';
import { useTranslation } from 'react-i18next';
import { Redirect } from 'react-router-dom';
import LocaleContext from '../../contexts/LocaleContext';
import UserContext from '../../contexts/UserContext';
import { API } from '../../lib/API';
import { BASE_URL, STATUS } from '../../lib/Constraints';
import { errorFor, fireGlobalErrors } from '../../lib/Errors';
import JWT from '../../lib/JWT';
import ErrorInput from '../ErrorInput/ErrorInput';

const initialErrors = {...errorFor('email'), ...errorFor('password')};

function SignIn() {
    const [user, setUser] = React.useState({email: "", password: ""});
    const [redirect, setRedirect] = React.useState({should: false, to: "/"});
    const [errors, setErrors] = React.useState(initialErrors);

    const { userDispatch } = React.useContext(UserContext);
    const locale = React.useContext(LocaleContext);
    const { t } = useTranslation('auth');

    const changeHandler = (event) => {
        setUser({...user, [event.target.name]: event.target.value})
    };
    
    const okCallback = React.useCallback((user) => {
        JWT.clearStorage();
        let headers = new Headers();
        headers.set('Content-Type', 'application/json');
        headers.set('Accept-Language', locale.full);
        let body = user;

        API.post({
            endpoint: BASE_URL, 
            url: 'signin', 
            headers: headers, 
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 200) {
                let token = response.headers.get('Authorization');
                userDispatch({type: 'signin', token: token});
                setRedirect({should: true, to: "/"});
                setErrors(initialErrors);
            } else {
                response.json().then((result) => {
                    if (result.status === STATUS.FAILURE) {
                        let newErrors = {
                            ...errorFor(
                                'email',
                                result.errorData
                                    .filter((value) => value.field === 'email')
                                    .map((value) => value.message)
                            ),
                            ...errorFor(
                                'password',
                                result.errorData
                                    .filter((value) => value.field === 'password')
                                    .map((value) => value.message)
                            )
                        }

                        setErrors(newErrors);

                        fireGlobalErrors(result.errorData);
                    }
                });
            }
        });
    }, [userDispatch, locale.full]);

    return (
        <div className="container auth-form">
            <div className="d-flex flex-column">
                <div className="form-group">
                    <label htmlFor="email">
                        { t('label.email') }
                    </label>

                    <ErrorInput
                        type="email" 
                        className="form-control"
                        id="email" 
                        placeholder={ t('placeholder.email') }
                        value={user.email}
                        name="email"
                        onChange={changeHandler}
                        errors={errors.email}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password">
                        { t('label.password') }
                    </label>

                    <ErrorInput 
                        type="password" 
                        className="form-control" 
                        id="password" 
                        placeholder={ t('placeholder.password') } 
                        value={user.password}
                        name="password"
                        onChange={changeHandler}
                        errors={errors.password}
                    />
                </div>
                
                <div className="d-flex flex-row justify-content-end">
                    <button 
                        className="btn btn-primary" 
                        onClick={() => {
                            okCallback(user);
                        }}
                    >
                        { t('button.signin') }
                    </button>
                </div>
            </div>
            {redirect.should && <Redirect to={redirect.to}/>}
        </div>
    );
}

export default SignIn;