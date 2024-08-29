import logo from './logo.svg';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RegistrationForm from './components/RegistrationForm';
import './App.css';
import DataDisplay from "./components/DataDisplay";
import UserDetail from "./components/UserDetail";
import LoginPage from "./components/LoginPage";
import Protected from "./components/Protected";
import Signout from "./components/SignOut";

function App() {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<DataDisplay />} />
                    <Route path="/protected" element={<Protected />} />
                    <Route path="/signup" element={<RegistrationForm />} />
                    <Route path="/users/:id" element={<UserDetail />} />
                    <Route path="/signin" element={<LoginPage />} />
                    <Route path="/signout" element={<Signout />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
