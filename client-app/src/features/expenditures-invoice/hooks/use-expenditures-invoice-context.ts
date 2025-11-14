import { useContext } from 'react';
import { ExpendituresInvoiceContext, type ExpendituresInvoiceContextType } from '../context';

export const useExpendituresInvoiceContext = (): ExpendituresInvoiceContextType => {
  const context = useContext(ExpendituresInvoiceContext);
  if (!context) {
    throw new Error('useExpendituresInvoiceContext must be used within ExpendituresInvoiceProvider');
  }
  return context;
};
