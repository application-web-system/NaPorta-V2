import { Outlet } from 'react-router-dom';

export function AdminLayout() {
  return (
    <>
      {/* header down below */}
      <h1>Layout Admin</h1>
      <Outlet />
      {/* footer down below */}
    </>
  );
}
