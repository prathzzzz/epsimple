import { useContext } from 'react';
import { PayeeContext, type PayeeContextType } from '../context/payee-context';

export function usePayee(): PayeeContextType {
  const context = useContext(PayeeContext);
  if (!context) {
    throw new Error('usePayee must be used within a PayeeProvider');
  }
  return context;
}
