import React from 'react';
import { Redirect } from 'react-router-dom';
import LocaleContext from '../../contexts/LocaleContext';
import { API } from '../../lib/API';
import { STATUS } from '../../lib/Constraints';
import { errorFor, fireGlobalErrors } from '../../lib/Errors';
import QuestionForm from './QuestionForm';

const initialErrors = {...errorFor('link'), ...errorFor('title')};

function QuestionAdd() {
    const [errors, setErrors] = React.useState(initialErrors);
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const locale = React.useContext(LocaleContext);

    const okCallback = React.useCallback((question) => {
        let headers = new Headers();
        headers.set('Content-Type', 'application/json');
        headers.set('Accept-Language', locale.full);

        let body = {
            ...question,
            locale: locale.short.toUpperCase()
        };

        API.post({ 
            headers: headers,
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.status === 201) {
                setShouldRedirect(true);
            } else {
                response.json().then((result) => {
                    if (result.status === STATUS.FAILURE) {
                        let newErrors = {
                            ...errorFor(
                                'link',
                                result.errorData
                                    .filter((value) => value.field === 'link')
                                    .map((value) => value.message)
                            ),
                            ...errorFor(
                                'title',
                                result.errorData
                                    .filter((value) => value.field === 'title')
                                    .map((value) => value.message)
                            )
                        }

                        setErrors(newErrors);

                        fireGlobalErrors(result.errorData);
                    }
                });
            }
        });

    }, [locale.full, locale.short]);

    const cancelCallback = React.useCallback(() => {
        setShouldRedirect(true);
    }, []);

    return (
        <>
            <div className="container">
                <QuestionForm
                    okCallback={okCallback}
                    cancelCallback={cancelCallback}
                    errors={errors}
                />
            </div>
            
            {shouldRedirect && <Redirect to="/" />}
        </>
    );
}

export default QuestionAdd;