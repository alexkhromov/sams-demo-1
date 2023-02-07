import React from 'react';
import { Redirect } from 'react-router-dom';
import LocaleContext from '../../contexts/LocaleContext';
import { API } from '../../lib/API';
import { LEVELS, STATUS } from '../../lib/Constraints';
import { errorFor, fireGlobalErrors } from '../../lib/Errors';
import QuestionForm from './QuestionForm';

const initialErrors = {
    ...errorFor('link'), 
    ...errorFor('titleRu'), 
    ...errorFor('titleEn')
};

function QuestionEdit({ match }) {
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const [question, setQuestion] = React.useState(undefined);
    const [errors, setErrors] = React.useState(initialErrors);

    const questionId = match.params.questionId || 0;
    const locale = React.useContext(LocaleContext);

    const okCallback = React.useCallback((question) => {
        let headers = new Headers();
        headers.set('Content-Type', 'application/json');
        headers.set('Accept-Language', locale.full);

        let body = {
            link: question.link,
            level: question.level,
            titles: [
                {
                    locale: "RU",
                    title: question.titleRu
                },
                {
                    locale: "EN",
                    title: question.titleEn
                }
            ]
        };

        API.put({
            url: `${questionId}`,
            headers: headers,
            body: JSON.stringify(body)
        }).then((response) => {
            if (response.ok) {
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
                                'titleRu',
                                result.errorData
                                    .filter((value) => value.field === 'titles[0].title')
                                    .map((value) => value.message)
                            ),
                            ...errorFor(
                                'titleEn',
                                result.errorData
                                    .filter((value) => value.field === 'titles[1].title')
                                    .map((value) => value.message)
                            )
                        }

                        setErrors(newErrors);

                        fireGlobalErrors(result.errorData);
                    }
                });
            }
        });

    }, [questionId, locale.full]);

    const cancelCallback = React.useCallback(() => {
        setShouldRedirect(true);
    }, []);

    React.useEffect(() => {

        let headers = new Headers();
        headers.set('Accept-Language', locale.full);
        
        API.get({
            url: `${questionId}`,
            headers: headers
        }).then((response) => {
            if (response.ok) {
                response.json().then((value) => {
                    let data = value.data[0];
                    let filterRu = data.titles.filter((value) => value.locale === "RU");
                    let filterEn = data.titles.filter((value) => value.locale === "EN");
                    let titleRu = (filterRu && filterRu[0] && filterRu[0].title) || "";
                    let titleEn = (filterEn && filterEn[0] && filterEn[0].title) || "";

                    let question = {
                        link: data.link,
                        level: data.level,
                        titleRu: titleRu,
                        titleEn: titleEn,
                        isOwner: data.isOwner
                    };
                    
                    setQuestion(question);
                });
            } else {
                response.json().then((result) => {
                    if (result.status === STATUS.FAILURE) {
                        fireGlobalErrors(result.errorData);
                    }
                });

                setQuestion({titleRu: "", titleEn: "", link: "", level: LEVELS[0].filter, isOwner: false});
            }
        });

    }, [questionId, locale.full]);

    return (
        <>
            <div className="container">
                <QuestionForm
                    okCallback={okCallback}
                    cancelCallback={cancelCallback}
                    initState={question}
                    errors={errors}
                />
            </div>
            
            {shouldRedirect && <Redirect to="/" />}
        </>
    );

}

export default QuestionEdit;