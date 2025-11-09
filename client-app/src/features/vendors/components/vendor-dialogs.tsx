import { useQueryClient } from '@tanstack/react-query'
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog'
import { useVendorContext } from '../vendor-provider'

export function VendorDialogs() {
  const queryClient = useQueryClient()
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } = useVendorContext()

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['vendors'] })
  }

  const bulkUploadConfig = {
    entityName: 'Vendor',
    uploadEndpoint: '/api/vendors/bulk-upload',
    templateEndpoint: '/api/vendors/download-template',
    exportEndpoint: '/api/vendors/export',
    errorReportEndpoint: '/api/vendors/export-errors',
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
