import { useState, useEffect } from "react";

//Time to show the message
const duration = 3000;

//This adds a timer of 3 seconds to show the message of the error or success at deleting the event
export function useTemporaryMessage() {
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  useEffect(() => {
    if (error || success) {
      const timer = setTimeout(() => {
        setError(null);
        setSuccess(null);
      }, duration);
      return () => clearTimeout(timer);
    }
  }, [error, success]);

  return { error, setError, success, setSuccess };
}