export function errorFor(name, messages = []) {
    return {
        [name]: {
            enabled: messages.length > 0,
            messages: [...messages]
        }
    };
}

export function fireGlobalErrors(errors) {
    errors.filter((value) => !value.field).map((value) => alert(value.message));
}