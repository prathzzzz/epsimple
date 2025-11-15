import { useContext } from 'react';
import { ExpendituresInvoiceContext, type ExpendituresInvoiceContextType } from '../context';

export const useExpendituresInvoice = (): ExpendituresInvoiceContextType => {
  const context = useContext(ExpendituresInvoiceContext);
  if (!context) {
    throw new Error('useExpendituresInvoice must be used within ExpendituresInvoiceProvider');
  }
  return context;
};
