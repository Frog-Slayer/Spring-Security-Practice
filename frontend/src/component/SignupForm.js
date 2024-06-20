import React, { useState } from 'react'
import {Routes, Route, useNavigate}  from 'react-router-dom'
import axios from 'axios'

function SignupForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [accessToken, setAccesstoken] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        const body = {
            username: username,
            password: password
        };

        axios.post('http://localhost:8080/member/register', body)
            .then((res) => {
                if (res.status === 201) {
                    console.log("yes")
                }
            })
            .catch((res) => {
                console.log(res);
            })
    }

    const handleLogin = async (e) => {
        e.preventDefault();
        const body = {
            username: username,
            password: password
        };

        axios.post('http://localhost:8080/login', body,
            { withCredentials: true }, 
        )
            .then((res) => {
                if (res.status === 200) {
                    let accessToken = res.headers['authorization'];
                    setAccesstoken(accessToken);
                    // console.log(accessToken);
                    console.log(res);
                }
            })
            .catch((res) => {
                console.log(res);
            })
    }
    
    const handleAuthenticatedApiButton = async (e) => {
        e.preventDefault();

        axios.get('http://localhost:8080/member/test', {
            headers: {
                Authorization: accessToken
            },
        })
            .then((res) => {
                if (res.status === 200) {
                    console.log(res.data)
                }
            })
            .catch((res) => {
                console.log(res);
            })
    }

    const handleRefresh = async (e) => {
        e.preventDefault();

        axios.get('http://localhost:8080/refresh', { withCredentials: true })
            .then((res) => {
                if (res.status === 200) {
                    let accessToken = res.headers['authorization'];
                    setAccesstoken(accessToken);
                    console.log(accessToken)
                }
            })
            .catch((res) => {
                console.log(res);
            })
    }


    return (
        <div className="formContainer">
            <form onSubmit={handleSubmit} className="form">
                <div className="formGroup">
                    <input className="formInput" onChange={e=>setUsername(e.target.value)} type="id" placeholder="이메일 입력" />
                </div>
                <div className="formGroup">
                    <input className="formInput" onChange={e=>setPassword(e.target.value)} type="password" placeholder="비밀번호 입력" />
                </div>
                <button onClick={handleSubmit} className='submitButton' type='submit'> sign up </button>
                <button onClick={handleLogin} className='submitButton' type='submit'> login </button>
                <button onClick={handleAuthenticatedApiButton} className='submitButton' type='button'> api </button>
                <button onClick={handleRefresh} className='submitButton' type='button'> refresh </button>
            </form>
        </div>
    )

}

export default SignupForm;