import {
  isRouteErrorResponse,
  Links,
  Meta,
  Outlet,
  Scripts,
  ScrollRestoration,
} from "react-router";

import type { Route } from "./+types/root";
import 'bootstrap/dist/css/bootstrap.min.css'
import '~/styles/main.css'


export function Layout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <head>
        <meta charSet="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <Meta />
        <Links />
        <link rel="icon" type="image/png" href="/TicketHub_icon.png" />
      </head>
      <body>
        {children}
        <ScrollRestoration />
        <Scripts />
      </body>
    </html>
  );
}

export default function App() {
  return <Outlet />;
}

export function ErrorBoundary({ error }: Route.ErrorBoundaryProps) {
  let message = "Oops!";
  let details = "An unexpected error occurred.";
  let status
  if (isRouteErrorResponse(error)) {
    status = error.status;
    if (status === 404) {
      message = "The requested page could not be found.";
      details = "Parece que te perdiste buscando tickets.";
    } else if (status === 500) {
      message = "500 - Error Interno";
      details = error.data?.message || "Vuelve en un rato, cuando las cosas se normalicen ;)";
    }
  } else if (error instanceof Error) {
    details = error.message;
  }

  return (
    //replic of the stetic of Main Layout
    <div className="d-flex flex-column min-vh-100 bg-light">
      <main className="flex-grow-1 d-flex align-items-center justify-content-center text-center p-5">
        <div className="error-card shadow-lg p-5 bg-white rounded border border-danger">
          <h1 className="display-1 fw-bold text-danger">{status}</h1>
          <h2 className="mb-4">{message}</h2>
          <p className="lead mb-4">{details}</p>
          
          <div className="btns">
            <button 
              onClick={() => window.location.reload()}
              className="btn btn-primary m-2"
            >
              Reintentar
            </button>
            <a href="/" className="btn btn-outline-secondary m-2">
              Volver al Inicio
            </a>
          </div>

          {import.meta.env.DEV && error instanceof Error && (
            <pre className="w-full p-4 overflow-x-auto">
              <code>{error.stack}</code>
            </pre>
          )}
        </div>
      </main>
    </div>
  );
}
