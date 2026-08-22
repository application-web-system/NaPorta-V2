import { createBrowserRouter } from 'react-router-dom';

import { AuthLayout } from './pages/_layouts/auth';
import { NotFound } from './pages/404';
import { SignIn } from './pages/auth/sign-in';
import { AppLayout } from './pages/_layouts/app';
import { Guest } from './pages/guest';
import { Home } from './pages/app/home';
import { AdminLayout } from './pages/_layouts/admin';

export const router = createBrowserRouter([
  {
    path: '/guest',
    element: <Guest />,
  },
  {
    path: '/admin',
    element: <AuthLayout />,
    children: [
      {
        path: 'sign-in',
        element: <SignIn />,
      },
    ],
  },
  {
    path: '/admin',
    element: <AdminLayout />,
    children: [
      {
        path: 'dashboard',
        // element: <SignIn />,
      },
      {
        path: 'orders',
        // element: <SignIn />,
      },
      {
        path: 'categories',
        // element: <SignIn />,
      },
    ],
  },
  {
    path: '/',
    element: <AppLayout />,
    children: [
      {
        path: '',
        element: <Home />,
      },
      {
        path: 'menu',
        // element: <Guest />,
      },
      {
        path: 'cart',
        // element: <Guest />,
      },
      {
        path: 'status',
        // element: <Guest />,
      },
    ],
  },
  {
    path: '*',
    element: <NotFound />,
  },
]);
