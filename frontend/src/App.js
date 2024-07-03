import logo from './logo.svg';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RegistrationForm from './components/RegistrationForm';
import './App.css';
import DataDisplay from "./DataDisplay";

function App() {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<DataDisplay />} />
                    <Route path="/register" element={<RegistrationForm />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
