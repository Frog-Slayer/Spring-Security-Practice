import React, {useEffect, useCallback} from 'react'
import {Routes, Route, useNavigate}  from 'react-router-dom'
import axios from 'axios'

function SignupForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        const body = {
            email: email,
            password: password
        };

        axios.post('localhost:8080/member/register', body)
            .then((res) => {
                if (res.status === 201) {
                    navigate('/login')
                }
            })
            .catch((res) => {
                console.log(res);
            })
    }
}

export default SignupForm;