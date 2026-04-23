import { useState, useEffect } from "react";

export function useTemporaryMessage(duration = 3000) {
  const [error, setErrorState] = useState<string | null>(null);
  const [success, setSuccessState] = useState<string | null>(null);

  useEffect(() => {
    if (!error) return;
    const t = setTimeout(() => setErrorState(null), duration);
    return () => clearTimeout(t);
  }, [error, duration]);

  useEffect(() => {
    if (!success) return;
    const t = setTimeout(() => setSuccessState(null), duration);
    return () => clearTimeout(t);
  }, [success, duration]);

  return {
    error,
    setError: setErrorState,
    success,
    setSuccess: setSuccessState,
  };
}
