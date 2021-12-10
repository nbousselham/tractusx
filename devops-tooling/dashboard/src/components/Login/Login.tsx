import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
//import Link from '@mui/material/Link';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { ThemeProvider } from '@mui/material/styles';
import theme from '../../Theme';
import useAuth from '../../Auth/useAuth';
import { useLocation, useNavigate } from 'react-router-dom';
import { useState } from 'react';

const defaultValues = {username: '', password: ''};

export default function Login() {
  const required = "This field is required.";
  //const navigate = useNavigate();
  const auth = useAuth();
  let navigate = useNavigate();
  let location = useLocation();
  let from = location.state?.from?.pathname || '/dashboard';

  const [values, setValues] = useState(defaultValues);
  const [errors, setErrors] = useState(defaultValues);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if(fieldHasError(errors.username) || fieldHasError(errors.password)) return;
    const data = new FormData(event.currentTarget);
    let username = data.get('username');
    if(typeof username === 'string') {
      auth.signin(username,()=> {console.log("asd"); navigate(from, { replace: true });});
    }
  };

  const handleInputChange = e => {
    const { name, value } = e.target;
    setValues({...values, [name]: value});
  }

  const validate = () => {
    const temp = {... errors};
    temp.username = values.username ? '' : required
    temp.password = values.password ? '' : required

    setErrors({...temp});
  }

  const fieldHasError = (type) => type.length > 0;

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
              onChange={handleInputChange}
              error={fieldHasError(errors.username)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              onChange={handleInputChange}
              error={fieldHasError(errors.password)}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onMouseOver={() => validate()}
            >
              Sign In
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
