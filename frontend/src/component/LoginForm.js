import React, { useState } from 'react'
import {Routes, Route, useNavigate}  from 'react-router-dom'
import axios from 'axios'

function LoginForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        const body = {
            username: username,
            password: password
        };

        axios.post('http://localhost:8080/member/register', body)
            .then((res) => {
                if (res.status === 200) {
                    console.log("yes")
                }
            })
            .catch((res) => {
                console.log(res);
            })
    }

    return (
        <div className="formContainer">
            <form onSubmit={handleLogin} className="form">
                <div className="formGroup">
                    <input className="formInput" onChange={e=>setUsername(e.target.value)} type="id" placeholder="이메일 입력" />
                </div>
                <div className="formGroup">
                    <input className="formInput" onChange={e=>setPassword(e.target.value)} type="password" placeholder="비밀번호 입력" />
                </div>
                <button onClick={handleLogin} className='submitButton' type='submit'> sign up </button>
            </form>
        </div>
    )

}

export default LoginForm;