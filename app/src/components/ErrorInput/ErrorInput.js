import React from 'react';
import classNames from 'classnames';

function ErrorInput({errors, className, ...otherProps}) {
    let inputClass = classNames(
        {
            'is-invalid': errors.enabled
        },
        className
    );

    return (
        <>
            <input
                {...otherProps}
                className={inputClass}
            />

            {
                errors.enabled 
                && (
                    <div className="invalid-feedback">
                        {errors.messages.map((value, index) => {
                            return (
                                <>
                                    {(index > 0) && <br />}
                                    {value}
                                </>
                            );
                        })}
                    </div>
                )
            }
        </>
    )
}

export default ErrorInput;