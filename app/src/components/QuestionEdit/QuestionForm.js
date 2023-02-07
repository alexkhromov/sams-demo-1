import React from 'react';
import { useTranslation } from 'react-i18next';
import UserContext from '../../contexts/UserContext';
import { LEVELS, ROLE } from '../../lib/Constraints';
import ErrorInput from '../ErrorInput/ErrorInput';

function QuestionForm({ initState, okCallback, cancelCallback, errors }) {
    const [question, setQuestion] = React.useState(initState || {titleRu: "", titleEn: "", link: "", level: LEVELS[0].filter, isOwner: false});
    const { t } = useTranslation('forms');
    const { userState } = React.useContext(UserContext);
    
    const changeHandler = (event) => {
        setQuestion({...question, [event.target.name]: event.target.value});
    };

    React.useEffect(() => {
        if (initState) {
            setQuestion(initState);
        }
    }, [initState]);

    const isDisabled = !(
        userState.loggedIn
        && (
            (userState.roles.includes(ROLE.USER) && (!question || (question && question.isOwner)))
            || (userState.roles.includes(ROLE.TRANSLATOR))
            || (userState.roles.includes(ROLE.MODERATOR) && (!question || (question && question.isOwner)))
            || (userState.roles.includes(ROLE.ADMIN))
        )
    );

    return (
        <div className="d-flex flex-column">
            <div className="input-group my-3">
                <div className="btn-group btn-group-toggle" id="question-level">
                    {LEVELS.map((level) => {
                        const isActive = level.filter === question.level;
                        return (
                            <button 
                                type="button"
                                className={`btn${isActive ? " btn-primary" : ""}`}
                                name="level"
                                value={level.filter}
                                onClick={changeHandler}
                                key={level.filter}
                                disabled={isDisabled}
                            >
                                { t(`level.${level.text}`) }
                            </button>   
                        );
                    })}
                </div>
            </div>

            <div className="form-group">
                <label htmlFor="question-link">
                    { t('label.question.answerLink') }
                </label>

                <ErrorInput 
                    type="text" 
                    className="form-control" 
                    id="question-link" 
                    placeholder={ t('placeholder.question.answerLink') }
                    value={question.link}
                    name="link"
                    onChange={changeHandler}
                    disabled={isDisabled}
                    errors={errors.link}
                />
            </div>

            <div className="form-group">
                <label htmlFor="question-titleEn">
                    { t('label.question.title.en') }
                </label>

                <ErrorInput 
                    type="text" 
                    className="form-control" 
                    id="question-titleEn" 
                    placeholder={ t('placeholder.question.title.en') } 
                    value={question.titleEn}
                    name="titleEn"
                    onChange={changeHandler}
                    disabled={isDisabled}
                    errors={errors.titleEn}
                />
            </div>

            <div className="form-group">
                <label htmlFor="question-titleRu">
                    { t('label.question.title.ru') }
                </label>

                <ErrorInput 
                    type="text" 
                    className="form-control" 
                    id="question-titleRu" 
                    placeholder={ t('placeholder.question.title.ru') } 
                    value={question.titleRu}
                    name="titleRu"
                    onChange={changeHandler}
                    disabled={isDisabled}
                    errors={errors.titleRu}
                />
            </div>
            
            <div className="d-flex flex-row justify-content-end">
                <button 
                    className="btn btn-primary" 
                    onClick={() => {
                        okCallback(question);
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

export default QuestionForm;