import { createBrowserRouter } from 'react-router-dom';

import { AuthLayout } from './pages/_layouts/auth';
import { NotFound } from './pages/404';
import { SignIn } from './pages/auth/sign-in';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <AuthLayout />,
    children: [
      {
        path: '/',
        element: <SignIn />,
      },
    ],
  },
  {
    path: '*',
    element: <NotFound />,
  },
]);
