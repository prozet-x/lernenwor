import jwtDecode from 'jwt-decode';

const isTokenExpired = (token) => {
    if (!token) return true;

    const { exp } = jwtDecode(token);
    const now = Date.now() / 1000;
    return exp < now;
};

export { isTokenExpired };