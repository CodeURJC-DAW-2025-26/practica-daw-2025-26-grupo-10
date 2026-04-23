import { useState, useRef } from "react";

export function useConfirmDialog() {
  const [isOpen, setIsOpen] = useState(false);
  const [message, setMessage] = useState("");
  const callbackRef = useRef<(() => Promise<void>) | null>(null);

  function confirm(msg: string, callback: () => Promise<void>) {
    setMessage(msg);
    callbackRef.current = callback;
    setIsOpen(true);
  }

  function handleCancel() {
    setIsOpen(false);
    callbackRef.current = null;
  }

  async function handleConfirm() {
    setIsOpen(false);
    if (callbackRef.current) {
      await callbackRef.current();
      callbackRef.current = null;
    }
  }

  return { isOpen, message, confirm, handleCancel, handleConfirm };
}
