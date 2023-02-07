import React from 'react';
import { useTranslation } from 'react-i18next';
import { LEVELS } from '../../lib/Constraints';
import ErrorInput from '../../components/ErrorInput/ErrorInput'

function QuestionForm({ okCallback, cancelCallback, errors }) {
    const [question, setQuestion] = React.useState({title: "", link: "", level: LEVELS[0].filter});
    const { t } = useTranslation('forms');
    
    const changeHandler = (event) => {
        setQuestion({...question, [event.target.name]: event.target.value})
    };

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
                    errors={errors.link}
                />
            </div>

            <div className="form-group">
                <label htmlFor="question-title">
                    { t('label.question.title.common') }
                </label>

                <ErrorInput 
                    type="text" 
                    className="form-control" 
                    id="question-title" 
                    placeholder={ t('placeholder.question.title.common') } 
                    value={question.title}
                    name="title"
                    onChange={changeHandler}
                    errors={errors.title}
                />
            </div>
            
            <div className="d-flex flex-row justify-content-end">
                <button 
                    className="btn btn-primary" 
                    onClick={() => {
                        okCallback(question);
                    }}
                >
                    { t('add.ok') }
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