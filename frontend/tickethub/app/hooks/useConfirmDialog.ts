import { useState } from "react";

export function useConfirmDialog() {
  const [isNotConfirmed, setIsNotConfirmed] = useState(false);
  const [message, setMessage] = useState("");
  const [onConfirmCallback, setOnConfirmCallback] = useState<() => void>(() => {});

  function confirm(message: string, onConfirm: () => void) {
    setMessage(message);
    setOnConfirmCallback(() => onConfirm);
    setIsNotConfirmed(true);
  }

  function handleConfirm() {
    onConfirmCallback();
    setIsNotConfirmed(false);
  }

  function handleCancel() {
    setIsNotConfirmed(false);
  }

  return { isOpen: isNotConfirmed, message, confirm, handleConfirm, handleCancel };
}