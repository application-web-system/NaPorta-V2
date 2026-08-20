import { Outlet } from 'react-router-dom';

export function AuthLayout() {
  return (
    <>
      <h1>Layout Auth</h1>
      <Outlet />
    </>
  );
}
