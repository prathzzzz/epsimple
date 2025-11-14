import { useQueryClient } from '@tanstack/react-query';
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog';
import { useExpendituresInvoiceContext } from '../hooks/use-expenditures-invoice-context';

export function ExpendituresInvoiceDialogs() {
  const queryClient = useQueryClient();
  const {
    isBulkUploadDialogOpen,
    setIsBulkUploadDialogOpen,
  } = useExpendituresInvoiceContext();

  return (
    <>
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          entityName: 'Expenditures Invoice',
          uploadEndpoint: '/api/expenditures/invoices/bulk-upload',
          errorReportEndpoint: '/api/expenditures/invoices/bulk-upload/errors',
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['expenditures-invoices'] });
          },
        }}
      />
    </>
  );
}
