import { Outlet } from 'react-router-dom';

export function AppLayout() {
  return (
    <>
      {/* header down below */}
      <h1>Layout App</h1>
      <Outlet />
      {/* footer down below */}
    </>
  );
}
