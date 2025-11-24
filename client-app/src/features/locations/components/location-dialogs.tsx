import { useQueryClient } from '@tanstack/react-query'
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog'
import { useLocation } from '../context/location-provider'

export function LocationDialogs() {
  const { 
    isBulkUploadDialogOpen,
    closeBulkUploadDialog,
  } = useLocation()
  const queryClient = useQueryClient()

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['locations'] })
  }

  const bulkUploadConfig = {
    entityName: 'Location',
    uploadEndpoint: '/api/locations/bulk-upload',
    errorReportEndpoint: '/api/locations/export-errors',
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
