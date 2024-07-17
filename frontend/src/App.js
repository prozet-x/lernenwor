import logo from './logo.svg';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RegistrationForm from './components/RegistrationForm';
import './App.css';
import DataDisplay from "./components/DataDisplay";
import UserDetail from "./components/UserDetail";

function App() {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<DataDisplay />} />
                    <Route path="/register" element={<RegistrationForm />} />
                    <Route path="/users/:id" element={<UserDetail />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
