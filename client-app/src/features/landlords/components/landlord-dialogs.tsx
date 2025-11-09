import { useQueryClient } from '@tanstack/react-query'
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog'
import { useLandlordContext } from '../landlord-provider'

export function LandlordDialogs() {
  const queryClient = useQueryClient()
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } = useLandlordContext()

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['landlords'] })
  }

  const bulkUploadConfig = {
    entityName: 'Landlord',
    uploadEndpoint: '/api/landlords/bulk-upload',
    templateEndpoint: '/api/landlords/download-template',
    exportEndpoint: '/api/landlords/export',
    errorReportEndpoint: '/api/landlords/export-errors',
    onSuccess: handleBulkUploadSuccess,
  }

  return (
    <>
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  )
}
